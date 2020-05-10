import { Component, OnInit } from '@angular/core';
import { ChatService } from 'src/app/_services/chat.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FriendProfile } from 'src/app/_dtos/chat/FriendProfile';
import { UserProfile } from 'src/app/_dtos/user/UserProfile';
import { UserService } from 'src/app/_services/user.service';
import { NbMessage } from 'src/app/_dtos/chat/NbMessage';
import { Observable } from 'rxjs';
import { UserMessage } from 'src/app/_dtos/chat/UserMessage';

@Component({
  selector: 'app-chat-detail',
  templateUrl: './chat-detail.component.html',
  styleUrls: ['./chat-detail.component.scss']
})
export class ChatDetailComponent implements OnInit {

  messages: NbMessage[] = [];
  friendId: string;
  friendProfile: FriendProfile;
  myProfile: UserProfile;
  subscription: any;

  constructor(private chatService: ChatService, private router: Router, private route: ActivatedRoute, private userService: UserService) {

    this.route.params.subscribe(params => {
      this.friendId = params['id'];
      if (this.subscription) this.subscription.unsubscribe();
      this.myProfile = this.userService.getProfile();
      this.friendProfile = this.chatService.getFriend(this.friendId);
      this.getChat()
    });
  }

  ngOnInit(): void {
  }

  getChat() {
    this.messages = []
    this.subscription = this.chatService.getMessages(this.friendId).subscribe(msgs => {
      let messages = msgs.map(msg => {
        let nm = new NbMessage(msg)
        if (msg.senderId == this.myProfile.id) nm.updateUser(this.myProfile.name, this.myProfile.imgUrl, true)
        else nm.updateUser(this.friendProfile.name, this.friendProfile.imgUrl, false)
        return nm
      })
      this.messages.push(...messages.slice(this.messages.length, messages.length))
    })
  }

  sendMessage(event) {
    const files = !event.files ? [] : event.files;

    let formData = new FormData();

    if(files.length == 0){
      this.chatService.createMessageText(this.friendId, event.message).subscribe()
    } else {
      formData.append('files', files);
      this.chatService.createMessageFile(this.friendId, event.message, formData).subscribe()
    }
  }



}
