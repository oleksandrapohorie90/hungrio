import { createContext, useContext, useMemo } from "react";

type AuthCtx = { token: string | null };
const AuthContext = createContext<AuthCtx>({ token: null });

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const token = typeof window !== "undefined" ? localStorage.getItem("token") : null;
  const value = useMemo(() => ({ token }), [token]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);
