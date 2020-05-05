import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { FriendProfile } from '../_dtos/chat/FriendProfile';
import { DataService } from './data.service';
import { UserMessage } from '../_dtos/chat/UserMessage';

@Injectable()
export class ChatService {

  private _fetch: BehaviorSubject<number> = new BehaviorSubject(0);
  public readonly fetch: Observable<number> = this._fetch.asObservable();

  httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

  constructor(private httpClient: HttpClient, private dataService: DataService) { }

  fetchFriends(): Observable<any> {
    return this.httpClient.get(`${environment.DOMAIN}/api/users/chat`, this.httpOptions)
      .pipe(map((friends: FriendProfile[]) => {
        this.dataService.updateFriends(friends)
      }))
  }

  updateFetch(value){
    this._fetch.next(value)
  }

  createFriend(email: String): Observable<any> {
    return this.httpClient.get(`${environment.DOMAIN}/api/users/chat/new?email=${email}`, this.httpOptions)
      .pipe(map((friend: FriendProfile) => {
        this.dataService.updateFriends([friend])
      }))
  }

  fetchMessages(covId: string): Observable<any> {
    return this.httpClient.get(`${environment.DOMAIN}/api/users/chat/${covId}/messages`, this.httpOptions)
      .pipe(map((msgs: UserMessage[]) => {
        this.dataService.updateUserMessages(msgs)
      }))
  }

  getFriends(): Observable<FriendProfile[]>{
    return this.dataService.getFriends()
  }

  getMessages(covId: string): Observable<UserMessage[]>{
    return this.dataService.getMessages(covId)
  }

}
