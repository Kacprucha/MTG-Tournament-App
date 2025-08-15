import 'next-auth/jwt'
import 'next-auth'

// Rozszerzamy typ JWT, aby zawierał accessToken
declare module 'next-auth/jwt' {
  interface JWT {
    accessToken?: string
    idToken?: string
    userRoles?: string[]
    username?: string
  }
}

// Rozszerzamy typ Session, aby zawierał accessToken
declare module 'next-auth' {
  interface Session {
    accessToken?: string
    idToken?: string 
    user?: {
      username?: string
      roles?: string[] 
    } & DefaultSession['user']
  }

  interface User {
    username?: string
    roles?: string[]
  }
}