import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service';

import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public token: Observable<String>;
  private tokenSubject: BehaviorSubject<String>;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) {
    this.tokenSubject = new BehaviorSubject<String>(tokenStorage.getToken())
    this.token = this.tokenSubject.asObservable();
  }

  getToken(): String {
    this.tokenSubject.next(this.tokenStorage.getToken())
    return this.tokenSubject.value;
  }

  login(credentials): Observable<any> {
    return this.http.post(`${environment.DOMAIN}/api/account/signin`, credentials, this.httpOptions).pipe(map(user => {
      this.tokenStorage.saveToken(user['accessToken']); return user
    }));
  }

  register(user): Observable<any> {
    return this.http.post(`${environment.DOMAIN}/api/account/signup`, user, this.httpOptions);
  }

  logout() {
    this.tokenStorage.signOut()
  }
}
