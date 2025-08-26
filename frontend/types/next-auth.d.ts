import 'next-auth/jwt'
import 'next-auth'

// Rozszerzamy typ JWT, aby zawierał accessToken
declare module 'next-auth/jwt' {
  interface JWT {
    accessToken?: string
    idToken?: string
    accessTokenExpires?: number
    refreshToken?: string
    error?: string
    userRoles?: string[]
    username?: string
  }
}

// Rozszerzamy typ Session, aby zawierał accessToken oraz rozszerzamy domyślny typ Profile
declare module 'next-auth' {
  interface Profile {
    preferred_username?: string;
    realm_access?: {
      roles: string[];
    };
  }

  interface Session {
    accessToken?: string
    idToken?: string 
    error?: string
    user?: {
      id?: string;
      username?: string
      roles?: string[] 
    } & DefaultSession['user']
  }

  interface User {
    username?: string
    roles?: string[]
  }
}