public class SignupForm {

    @NotBlank
    private String name;

    @MaxLength(50)
    private String email;

    @NotBlank
    @MaxLength(10)
    private String username;

    public SignupForm(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }
}
