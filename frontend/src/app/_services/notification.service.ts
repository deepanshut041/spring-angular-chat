import { Injectable } from '@angular/core';
import { DataService } from './data.service';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { environment } from 'src/environments/environment';
import { UserService } from './user.service';
import { UserMessage } from '../_dtos/chat/UserMessage';
import { FriendProfile } from '../_dtos/chat/FriendProfile';

@Injectable()
export class NotificationService {

  stompClient: any;
  topic: string

  constructor(private dataService: DataService, private userService: UserService) {
    this.topic = `/notifications/${this.userService.getProfile().id}`
  }

  suscribe() {
    let ws = new SockJS(`${environment.DOMAIN}/ws`);
    this.stompClient = Stomp.over(ws);
    const _this = this;
    _this.stompClient.connect({}, function (frame) {
      _this.stompClient.subscribe(_this.topic, function (sdkEvent) {
        _this.onMessageReceived(sdkEvent);
      });
    }, this.errorCallBack);

  }

  errorCallBack(error) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this.suscribe();
    }, 5000);
  }

  onMessageReceived(message) {
    let json = JSON.parse(message.body)
    if(json['type'] == "USER_MESSAGE_ADDED" || json['type'] == "USER_MESSAGE_UPDATED"){
      let data = json['data'] as UserMessage
      this.dataService.updateUserMessages([data])
    } else if(json['type'] == "USER_CONVERSATION_UPDATED" || json['type'] == "USER_CONVERSATION_ADDED"){
      let data = json['data'] as FriendProfile
      this.dataService.updateFriends([data])
    } else{
      console.log(json)
    }
  }

}
