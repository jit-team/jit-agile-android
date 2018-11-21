package pl.jitsolutions.agile

sealed class Error {
    object InvalidEmail : Error()
    object EmptyEmail : Error()

    object InvalidPassword : Error()
    object WeakPassword : Error()
    object EmptyPassword : Error()

    object EmptyName : Error()
    object InvalidName : Error()

    object InvalidId : Error()

    object Exists : Error()
    object DoesNotExist : Error()

    object Network : Error()
    object Unknown : Error()
}