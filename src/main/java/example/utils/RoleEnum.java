package example.utils;

public enum RoleEnum {
    ADMIN(1),
    USER(2),
    GUEST(3);
    private final Integer roleId;

    RoleEnum(Integer roleName) {
        this.roleId = roleName;
    }

    public Integer getRoleId() {
        return roleId;
    }
}
