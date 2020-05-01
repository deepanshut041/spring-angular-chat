import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthComponent } from './auth.component';
import { SignupComponent } from './signup/signup.component';
import { SigninComponent } from './signin/signin.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { AuthRoutingModule } from './auth-routing.module';
import { SharedModule } from '../shared/shared.module';
import { TokenComponent } from './token/token.component';



@NgModule({
  declarations: [AuthComponent, SignupComponent, SigninComponent, ForgotPasswordComponent, ResetPasswordComponent, TokenComponent],
  imports: [
    CommonModule, AuthRoutingModule, SharedModule
  ]
})
export class AuthModule { }
