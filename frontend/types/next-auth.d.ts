import 'next-auth/jwt'
import 'next-auth'

// Rozszerzamy typ JWT, aby zawierał accessToken
declare module 'next-auth/jwt' {
  interface JWT {
    accessToken?: string
    userRoles?: string[]
    idToken?: string
  }
}

// Rozszerzamy typ Session, aby zawierał accessToken
declare module 'next-auth' {
  interface Session {
    accessToken?: string
    idToken?: string 
    user?: {
      roles?: string[] 
    } & DefaultSession['user']
  }
  interface User {
    roles?: string[]
  }
}