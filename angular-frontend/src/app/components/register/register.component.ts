import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Role } from 'src/app/dto/role';
import { UserDto } from 'src/app/dto/user-dto';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { RegisterService } from 'src/app/services/register.service';
import { PasswordMatchValidator } from 'src/app/validators/PasswordMatchValidator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  roles: Array<Role> = []
  
  constructor(private registerService: RegisterService, private router: Router,
    private toastr: ToastrService) {
   }

  ngOnInit(): void {
    this.registerService.getRoles().subscribe(data => {
      this.roles = data
    }, error => {
      console.log(error)
    })
  }

  formGroup: FormGroup = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required,
       Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+$'),
      Validators.minLength(8)]),
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    confirmPassword: new FormControl('', [Validators.required])
  },
  {
    validators: PasswordMatchValidator.checkPasswords
  }
  )

  private getUserRole(): Role {
    for(const role of this.roles) {
      if(role.authority === 'USER') {
        return role
      }
    }
    return {id: '', authority: ''}
  }

  public registerUser() {
    this.registerService.register({
      username: this.formGroup.get('username')?.value,
      password: this.formGroup.get('password')?.value,
      firstName: this.formGroup.get('firstName')?.value,
      lastName: this.formGroup.get('lastName')?.value,
      id: '',
      registrationDate: '',
      role: this.getUserRole()
    }).subscribe(() => {
      this.toastr.success("User created successfully")
      this.goToLogin()
    }, error => {
      this.toastr.error(error.error.message);
    })
  }

  public goToLogin() {
    this.router.navigate(['login'])
  }

  get username() {
    return this.formGroup.get('username') as FormControl
  }

  get password() {
    return this.formGroup.get('password') as FormControl
  }

  get firstName() {
    return this.formGroup.get('firstName') as FormControl
  }

  get lastName() {
    return this.formGroup.get('lastName') as FormControl
  }

  get confirmPassword() {
    return this.formGroup.get('confirmPassword') as FormControl
  }
  }

