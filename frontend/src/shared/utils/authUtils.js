export const hasRole = (user, role) => user?.roles?.includes(role);
export const hasPermission = (user, permission) => user?.permissions?.includes(permission);