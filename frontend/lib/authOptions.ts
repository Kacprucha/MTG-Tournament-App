import { AuthOptions } from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak";
import { jwtDecode } from "jwt-decode"; 

// Definicja typów dla zdekodowanego tokena
interface DecodedToken {
    realm_access?: {
        roles?: string[];
    };
    preferred_username?: string;
}

export const authOptions: AuthOptions = {
    session: {
        strategy: "jwt",
    },
    providers: [ 
        KeycloakProvider({
            clientId: process.env.KEYCLOAK_ID!,
            clientSecret: process.env.KEYCLOAK_SECRET!,
            issuer: process.env.KEYCLOAK_ISSUER!,
        }), 
    ],

    callbacks: {
        async jwt({ token, account }) {
            if (account && account.access_token) {
                token.accessToken = account.access_token;
                token.idToken = account.id_token; 

                try {
                    const decodedToken = jwtDecode<DecodedToken>(account.access_token);

                    if (decodedToken.realm_access?.roles) {
                        token.userRoles = decodedToken.realm_access.roles;
                    }

                    if (decodedToken.preferred_username) {
                        token.username = decodedToken.preferred_username;
                    }
                } catch (error) {
                    console.error("Błąd dekodowania tokena JWT:", error);
                }
            }
            return token; 
        },

        async session({ session, token }) {
            session.accessToken = token.accessToken;
            session.idToken = token.idToken;

            if (session.user) {
                session.user.roles = token.userRoles;
                session.user.username = token.username;
            }

            return session; 
        },
    },
};