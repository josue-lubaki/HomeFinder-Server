package ca.josue_lubaki.tools

import ca.josue_lubaki.data.request.RegisterRequest

/**
 * @author Josue Lubaki
 * @version 1.0
 * @since 2023-03-04
 */

class Utils {
    companion object {
        fun requestRegisterValidator(request: RegisterRequest): Boolean {
            return request.username.isBlank()
                    || request.password.isBlank()
                    || request.email.isBlank()
                    || request.firstName.isBlank()
                    || request.lastName.isBlank()
        }
    }
}