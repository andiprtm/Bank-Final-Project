public class Login {
    String username;
    String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login() {
        if (username.equals("andi") && password.equals("cobalagi")) {
            System.out.flush();
            System.out.println("login berhasil");
            return true;
        } else {
            System.out.flush();
            System.out.println("login gagal");
            return false;
        }
    }
}
