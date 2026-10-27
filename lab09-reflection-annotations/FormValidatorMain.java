public class FormValidatorMain {

    public static void main(String[] args) {
        SignupForm validForm = new SignupForm("Aman Verma", "aman.verma@minibank.com", "aman.v");
        SignupForm invalidForm = new SignupForm("   ",
                "long.email.address@institutional-mail.example.com", "verylongusername123");

        System.out.println("Valid form errors   : " + FormValidator.validate(validForm));
        System.out.println("Invalid form errors : " + FormValidator.validate(invalidForm));
    }
}
