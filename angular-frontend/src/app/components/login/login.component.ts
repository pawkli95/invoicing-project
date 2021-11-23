import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private authenticationService: AuthenticationService, 
    private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
  }

  formGroup: FormGroup = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required])
  })

  login() {
    this.authenticationService.login(
      this.formGroup.get('username')?.value,
      this.formGroup.get('password')?.value
    ).subscribe(() => {
      this.toastr.success("Logged in successfully")
      this.router.navigate([''])
    }, error => {
      this.toastr.error(error.error.message)
    })
  }

  get username() {
    return this.formGroup.get('username') as FormControl
  }

  get password() {
    return this.formGroup.get('password') as FormControl
  }

}
