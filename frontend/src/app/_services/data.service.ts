import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { FriendProfile } from '../_dtos/chat/FriendProfile';
import { UserMessage } from '../_dtos/chat/UserMessage';
import { map } from 'rxjs/operators';

@Injectable()
export class DataService {

  private _friends: BehaviorSubject<Map<string, FriendProfile>> = new BehaviorSubject(new Map());
  public readonly friends: Observable<Map<string, FriendProfile>> = this._friends.asObservable();

  private _userMessages: BehaviorSubject<Map<string, UserMessage>> = new BehaviorSubject(new Map());

  updateUserMessages(msgs: UserMessage[]) {
    let oldMsgs = this._userMessages.value
    msgs.map(msg => oldMsgs.set(msg.id, msg))
    this._userMessages.next(oldMsgs)
    this.sortFriends()
  }

  updateFriends(newFriends: FriendProfile[]) {
    let friends = this._friends.value
    newFriends.map(v => {
      let friend = friends.get(v.id)
      if (friend) {
        friend.update(v.id, v.email, v.name, v.imgUrl, v.blockedBy, v.updatedAt)
      } else { friends.set(v.id, v) }
    })
    this._friends.next(friends)
  }

  sortFriends() {
    let msgs = this._userMessages.value
    let friends = this._friends.value

    msgs.forEach((msg, k) => {
      let friend = friends.get(msg.chatId)
      if (friend.lastMsgAt < msg.createdAt) { friend.updateConv(msg.content, msg.createdAt) }
      // if (!msg.readAt) { friend.incrementUnread() }
    })
    this._friends.next(friends)
  }

  getMessages(chatId: String): Observable<UserMessage[]> {
    return this._userMessages.pipe(
      map(m => {
        let msgs: UserMessage[] = []
        m.forEach((v, k) => { if (v.chatId == chatId) msgs.push(v) })
        // msgs.sort((a, b) => a.createdAt.getTime() - b.createdAt.getTime())
        return msgs
      })
    )
  }

  getFriends(): Observable<FriendProfile[]> {
    return this._friends.pipe(
      map(m => {
        let friends: FriendProfile[] = []
        m.forEach((v, k) => { friends.push(v) })
        // friends.sort((a, b) => a.lastMsgAt.getTime() - b.lastMsgAt.getTime())
        return friends
      })
    )
  }

  getAllFriend(): Map<string,FriendProfile> {
    return this._friends.value
  }

}
