import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, filter } from 'rxjs/operators';
import { FriendProfile } from '../_dtos/chat/FriendProfile';
import { DataService } from './data.service';
import { UserMessage } from '../_dtos/chat/UserMessage';

@Injectable()
export class ChatService {

  private _fetch: BehaviorSubject<number> = new BehaviorSubject(0);
  public readonly fetch: Observable<number> = this._fetch.asObservable();

  httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
  fileOptions = { headers: new HttpHeaders({ 'Content-Type': 'multipart/form-data' }) };

  constructor(private httpClient: HttpClient, private dataService: DataService) { }

  fetchFriends(): Observable<any> {
    return this.httpClient.get(`${environment.DOMAIN}/api/chat`, this.httpOptions)
      .pipe(map((friends: FriendProfile[]) => {
        this.dataService.updateFriends(friends)
      }))
  }

  updateFetch(value) {
    this._fetch.next(value)
  }

  fetchAllMessages(): Observable<any> {
    return this.httpClient.post(`${environment.DOMAIN}/api/chat/messages`,
      Array.from(this.dataService.getAllFriend().keys()), this.httpOptions)
      .pipe(map((msgs: UserMessage[]) => {
        this.dataService.updateUserMessages(msgs)
      }))
  }

  fetchMessages(covId: string): Observable<any> {
    return this.httpClient.get(`${environment.DOMAIN}/api/chat/${covId}/messages`, this.httpOptions)
      .pipe(map((msgs: UserMessage[]) => {
        this.dataService.updateUserMessages(msgs)
      }))
  }

  createFriend(email: String): Observable<any> {
    return this.httpClient.post(`${environment.DOMAIN}/api/chat?email=${email}`, this.httpOptions)
      .pipe(map((friend: FriendProfile) => {
        this.dataService.updateFriends([friend])
      }))
  }

  createMessageText(cid: string, content: string): Observable<UserMessage> {
    return this.httpClient
      .post(`${environment.DOMAIN}/api/chat/${cid}/messages/text?content=${content}`,{}, this.httpOptions)
      .pipe(map((v: UserMessage) => {
        this.dataService.updateUserMessages([v])
        return v
      }))
  }

  createMessageFile(cid: string, content: string, data:FormData): Observable<UserMessage> {
    return this.httpClient
      .post(`${environment.DOMAIN}/api/chat/${cid}/messages/files?content=${content}`, data)
      .pipe(map((v: UserMessage) => {
        this.dataService.updateUserMessages([v])
        return v
      }))
  }

  getFriends(): Observable<FriendProfile[]> {
    return this.dataService.getFriends()
  }

  getFriend(id: string): FriendProfile {
    return this.dataService.getAllFriend().get(id)
  }

  getMessages(covId: string): Observable<UserMessage[]> {
    return this.dataService.getMessages(covId)
  }

}
