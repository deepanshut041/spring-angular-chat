import { Injectable } from '@angular/core';
import { UserProfile } from '../_dtos/user/UserProfile';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  TOKEN_KEY = 'auth-token';
  USER_KEY = 'auth-user'

  constructor() { }

  signOut() {
    localStorage.clear();
  }

  public saveToken(token: string) {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  public getToken(): string {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  public saveUser(user: UserProfile) {
    localStorage.removeItem(this.USER_KEY);
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  public getUser(): UserProfile {
    let raw = JSON.parse(localStorage.getItem(this.USER_KEY));
    return (raw != null)? new UserProfile(raw['id'], raw['email'], raw['name'], raw['imgUrl'], ) : null
    
  }

}
