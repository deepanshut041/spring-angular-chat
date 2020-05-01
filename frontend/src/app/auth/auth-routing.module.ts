import { NgModule } from '@angular/core';

import { Routes, RouterModule } from '@angular/router';
import { AuthComponent } from './auth.component';
import { SigninComponent } from './signin/signin.component';
import { SignupComponent } from './signup/signup.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { TokenComponent } from './token/token.component';

const routes: Routes = [
  {path: 'auth', component: AuthComponent, children:[
      {path: 'signin', component: SigninComponent},
      {path: 'signup', component: SignupComponent},
      {path: 'forgot', component: ForgotPasswordComponent},
      {path: 'reset', component: ResetPasswordComponent},
      {path: 'token', component: TokenComponent},
      {path: '', redirectTo:'signin', pathMatch: 'full'}
  ]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AuthRoutingModule { }
