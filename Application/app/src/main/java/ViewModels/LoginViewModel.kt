package ViewModels

import Models.AuthorizationData
import Models.LoginRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import Views.LoginFragment
import com.example.myapplication.R
import kotlinx.coroutines.launch
import java.util.*

class LoginViewModel(val loginView: LoginFragment) : ViewModel()
{
    private val loginRepository = LoginRepository()

    init
    {
        loginView.onLoginButtonClicked += ::TryToLogin
    }

    override fun onCleared()
    {
        super.onCleared()

        if (loginView != null)
        {
            loginView.onLoginButtonClicked -= ::TryToLogin
        }
    }

    private fun TryToLogin(username: String, password: String)
    {
        viewModelScope.launch {
            val jsonInputString = "{\"login\": \"$username\", \"password\": \"$password\"}"
            //val jsonInputString = "{\"login\": \"janek\", \"password\": \"trudnehaslo\"}"

            AuthorizationData.SetAuthorizationKey(username, password)
            val result = loginRepository.MakeLoginRequest()
            if (result)
            {
                Navigation.findNavController(loginView.requireView()).navigate(R.id.action_login_to_board)
            }
            else
            {
                loginView.DisplayLoginError()
            }
        }
    }
}