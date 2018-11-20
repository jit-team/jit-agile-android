package pl.jitsolutions.agile

sealed class JitError {
    object InvalidEmail : JitError()
    object EmptyEmail : JitError()

    object InvalidPassword : JitError()
    object WeakPassword : JitError()
    object EmptyPassword : JitError()

    object EmptyName : JitError()
    object InvalidName : JitError()

    object InvalidId : JitError()

    object Exists : JitError()
    object DoesNotExist : JitError()

    object Network : JitError()
    object Unknown : JitError()
}