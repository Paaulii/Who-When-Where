package ViewModels

import Models.LoginRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import Views.LoginFragment
import com.example.myapplication.R
import kotlinx.coroutines.launch

class LoginViewModel(val loginView: LoginFragment) : ViewModel()
{
    private val loginRepository = LoginRepository()

    init {
        loginView.onLoginButtonClicked += ::TryToLogin
    }

    override fun onCleared() {
        super.onCleared()

        if (loginView != null){
            loginView.onLoginButtonClicked -= ::TryToLogin
        }
    }

    private fun TryToLogin(username: String, password: String)
    {
        viewModelScope.launch {
            //val jsonInputString = "{\"username\": \"$username\", \"password\": \"$password\"}"
            val jsonInputString = "{\"username\": \"kamil\", \"password\": \"toor\"}"

            val result = loginRepository.MakeLoginRequest(jsonInputString)
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