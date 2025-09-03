import NextAuth from "next-auth"
import { JWT } from "next-auth/jwt";
import KeycloakProvider from "next-auth/providers/keycloak"


async function refreshAccessToken(token: JWT) {
    try {
        const response = await fetch(`${process.env.KEYCLOAK_ISSUER}/protocol/openid-connect/token`, {
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            method: "POST",
            body: new URLSearchParams({
                client_id: process.env.KEYCLOAK_ID!,
                client_secret: process.env.KEYCLOAK_SECRET!,
                grant_type: "refresh_token",
                refresh_token: token.refreshToken!,
            }),
        })

        const refreshedTokens = await response.json();

        if (!response.ok) {
            throw refreshedTokens
        }

        return {
            ...token,
            accessToken: refreshedTokens.access_token,
            accessTokenExpires: Date.now() + refreshedTokens.expires_in * 1000,
            refreshToken: refreshedTokens.refresh_token ?? token.refreshToken,
        }
    } catch (error) {
    console.error("Error refreshing access token", error)
    return {
      ...token,
      error: "RefreshAccessTokenError",
    }
  }
}

const handler = NextAuth({
  providers: [
    KeycloakProvider({
      clientId: process.env.KEYCLOAK_ID!,
      clientSecret: process.env.KEYCLOAK_SECRET!,
      issuer: process.env.KEYCLOAK_ISSUER!,
    }),
  ],
  callbacks: {
    async jwt({ token, account, profile }) {
      // Przy pierwszym logowaniu
      if (account && profile) {
        const expiresIn = Number(account.expires_in ?? 300);

        token.idToken = account.id_token;
        token.accessToken = account.access_token;
        token.accessTokenExpires = Date.now() + expiresIn * 1000;
        token.refreshToken = account.refresh_token;

        token.sub = profile.sub;
        token.username = profile.preferred_username;
        token.name = profile.name;
        token.userRoles = profile.realm_access?.roles;
      }

      // Jeśli token jeszcze nie wygasł, zwróć go
      if (token.accessTokenExpires && Date.now() < (token.accessTokenExpires as number)) {
        return token
      }

      // Jeśli token wygasł, spróbuj go odświeżyć
      return refreshAccessToken(token)
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken as string;
      session.error = token.error as string; 

      if (!session.user) {
        session.user = {};
      }

      session.idToken = token.idToken;
      session.user.name = token.name;
      session.user.username = token.username;
      session.user.roles = token.userRoles;

      return session
    }
  }
})

export { handler as GET, handler as POST };