import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/_services/auth.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SignInRequest } from 'src/app/_dtos/auth/SignInRequest';
import { SignInResponse } from 'src/app/_dtos/auth/SignInResponse';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  loading: Boolean = false
  signInFrom: FormGroup
  redirect = "/"

  constructor( private _authService: AuthService, private fb: FormBuilder, private router: Router) { 
    this.signInFrom = this.fb.group({
      email: [],
      password: []
    })
  }

  ngOnInit(): void {
  }

  login(){
    if(this.signInFrom.valid){
      let data = this.signInFrom.value
      this.loading = true
      this._authService.login(new SignInRequest(data['email'], data['password'])).subscribe(
        (response: SignInResponse)=>{
          this.router.navigateByUrl(this.redirect)
          this.loading = false
        },(err:any)=>{
          this.loading = false
          console.log(err.error.message)
        }
      )
    }
  }

  facebook(){
    window.location.href=`${environment.DOMAIN}/oauth2/authorization/facebook?redirect_url=http://localhost:4200/auth/token`;
  }

  google(){
    window.location.href=`${environment.DOMAIN}/oauth2/authorization/google?redirect_url=http://localhost:4200/auth/token`;
  }

}
