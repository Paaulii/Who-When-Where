import Models.Task
import Models.TaskItemAdapter
import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.Rect
import android.view.DragEvent
import android.view.View
import android.widget.HorizontalScrollView
import androidx.core.math.MathUtils.clamp
import androidx.recyclerview.widget.RecyclerView
import java.lang.Thread.sleep
import kotlin.concurrent.thread


class DragListener   : View.OnDragListener {

    lateinit var horizontalScrollView: HorizontalScrollView
    var currentXPos: Int = 0
    var startAutoScroll: Boolean = false
    var scrollRight: Boolean = true
    var lastXPos : Int = 0

    companion object{
        private var listner: DragListener? = null


        fun GetInstance() : DragListener?{
            if (listner == null){
                listner = DragListener()
                return listner
            }
            else{
                return listner
            }
        }
    }

    fun SetScrollView(scrollView: HorizontalScrollView)
    {
        horizontalScrollView = scrollView
    }

    fun GetTouchPositionFromDragEvent(item: View, event: DragEvent): Point?
    {
        val rItem = Rect()
        item.getGlobalVisibleRect(rItem)
        return Point(rItem.left + Math.round(event.x), rItem.top + Math.round(event.y))
    }

    fun CheckIfAutoSwipeIsPossible(touchPosition: Point, scrollViewWidth: Int) : Boolean
    {
        if ((touchPosition!!.x >= scrollViewWidth - 70) && touchPosition!!.x > lastXPos)
        {
            return true
        }

        if ((touchPosition.x <= 5) && touchPosition!!.x < lastXPos)
        {
            return true
        }

        return false
    }


    override fun onDrag(v: View, event: DragEvent): Boolean {

        when (event.action) {
            DragEvent.ACTION_DRAG_LOCATION -> {
            val scrollView = horizontalScrollView
            val scrollViewWidth = scrollView.width
            val linearLayout = scrollView.getChildAt(0)
            val linearLayoutWidth = linearLayout.width
            val touchPosition = GetTouchPositionFromDragEvent(linearLayout, event)

            if (CheckIfAutoSwipeIsPossible(touchPosition!!, scrollViewWidth) && (!startAutoScroll))
            {
                startAutoScroll = true
                scrollRight = (touchPosition!!.x >= scrollViewWidth - 70)

                    thread {
                        sleep(1000L)
                        if ((touchPosition!!.x >= scrollViewWidth - 70) || (touchPosition.x <= 5) )
                        {
                            if (scrollRight)
                            {
                                currentXPos = clamp(currentXPos + linearLayoutWidth / 3 ,0, linearLayoutWidth - linearLayoutWidth/3)
                            }
                            else
                            {
                                currentXPos = clamp(currentXPos - linearLayoutWidth / 3 ,0, linearLayoutWidth - linearLayoutWidth/3)
                            }

                            scrollView.smoothScrollTo(
                                currentXPos,
                                0
                            )
                        }

                        sleep(1000)
                        startAutoScroll = false
                    }


            }

            lastXPos = touchPosition!!.x
        }

        DragEvent.ACTION_DROP -> {
        var positionTarget = -1
        val viewSource = event.localState as View?

        val target: RecyclerView = v.parent as RecyclerView
        positionTarget = v.tag as Int

        if (viewSource != null)
        {
            val source = viewSource.parent as RecyclerView
            val adapterSource = source.adapter as TaskItemAdapter?
            val positionSource = viewSource.tag as Int

            val list: Task? = adapterSource?.getList()?.get(positionSource)
            val listSource = adapterSource?.getList()?.apply {
                removeAt(positionSource)
            }

            listSource?.let { adapterSource.updateList(it) }
            adapterSource?.notifyDataSetChanged()

            val adapterTarget = target.adapter as TaskItemAdapter?
            val targetList = adapterTarget?.getList()

            if (positionTarget >= 0)
            {
                list?.let { targetList?.add(positionTarget, it) }
            } else
            {
                list?.let { targetList?.add(it) }
            }

            targetList?.let { adapterTarget.updateList(it) }
            adapterTarget?.notifyDataSetChanged()
        }
    } }
    return true
    }
}