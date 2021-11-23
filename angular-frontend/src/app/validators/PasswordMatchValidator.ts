import { AbstractControl, FormControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export class PasswordMatchValidator {
    static checkPasswords: ValidatorFn = (group: AbstractControl):  ValidationErrors | null => { 
        let passFormControl = group.get('password') as FormControl
        let pass = passFormControl.value
        let confirmPassFormControl = group.get('confirmPassword') as FormControl
        let confirmPass = confirmPassFormControl.value
        return pass === confirmPass ? null : { notSame: true }
      }
}