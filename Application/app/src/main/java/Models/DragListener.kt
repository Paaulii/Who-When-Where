import Models.Task
import Models.TaskItemAdapter
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.math.MathUtils.clamp
import androidx.core.view.ViewCompat.getRotation
import androidx.recyclerview.widget.RecyclerView
import java.lang.Thread.sleep
import java.util.Collections.min
import kotlin.concurrent.thread


class DragListener   : View.OnDragListener {

    lateinit var horizontalScrollView: HorizontalScrollView
    lateinit var linearLayout: LinearLayout
    var currentXPos: Int = 0
    var startAutoScroll: Boolean = false
    var scrollRight: Boolean = true
    var lastXPos : Int = 0

    var linearLayoutWidth : Int = 0
    var scrollViewWidth : Int = 0

    var rightEdge : Float = 0.0f
    var leftEdge : Float = 0.0f

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
        if ((touchPosition!!.x >= rightEdge) && touchPosition!!.x > lastXPos)
        {
            return true
        }

        if ((touchPosition.x <= leftEdge) && touchPosition!!.x < lastXPos)
        {
            return true
        }

        return false
    }

    override fun onDrag(v: View, event: DragEvent): Boolean {

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                val scrollView = horizontalScrollView
                scrollViewWidth = scrollView.width
                currentXPos = horizontalScrollView.scrollX
                linearLayout = scrollView.getChildAt(0) as LinearLayout
                linearLayoutWidth = linearLayout.width

                rightEdge = scrollViewWidth - (scrollViewWidth * 0.2f)
                leftEdge = scrollViewWidth * 0.2f
            }

            DragEvent.ACTION_DRAG_LOCATION -> {
            val touchPosition = GetTouchPositionFromDragEvent(linearLayout as View, event)

            if (CheckIfAutoSwipeIsPossible(touchPosition!!, scrollViewWidth) && (!startAutoScroll))
            {
                startAutoScroll = true
                scrollRight = (touchPosition!!.x >= rightEdge)

                   thread {
                        sleep(1000L)
                        if (touchPosition!!.x >= rightEdge|| (touchPosition.x <= leftEdge) )
                        {
                            if (scrollRight)
                            {
                                currentXPos = clamp(currentXPos + linearLayoutWidth / 3 ,0, linearLayoutWidth - linearLayoutWidth/3)
                            }
                            else
                            {
                                currentXPos = clamp(currentXPos - linearLayoutWidth / 3 ,0, linearLayoutWidth - linearLayoutWidth/3)
                            }

                            horizontalScrollView.smoothScrollTo(
                                currentXPos,
                                0
                            )
                        }

                        //sleep(1000)
                        startAutoScroll = false
                    }
            }

            lastXPos = touchPosition!!.x
        }

        DragEvent.ACTION_DROP -> {
        val viewSource = event.localState as View?

        var target : RecyclerView? = v.parent as? RecyclerView

        if (viewSource != null)
        {
            val source = viewSource.parent as RecyclerView
            val adapterSource = source.adapter as TaskItemAdapter?
            val positionSource = viewSource.tag as Int
            val task: Task = adapterSource?.getList()!![positionSource]

            if (target == null)
            {
                val linearLayout = v.parent as LinearLayout
                val count = linearLayout.getChildCount();

                for (i in 1 until count)
                {
                    target = linearLayout.getChildAt(i) as? RecyclerView;
                    if (target != null)
                    {
                        break
                    }
                }
            }

            val adapterTarget = target!!.adapter as TaskItemAdapter?
            val adapterState: String = target!!.tag as String
            adapterTarget!!.onChangeTaskState.invoke(task!!, adapterState)
        }
    } }
    return true
    }
}