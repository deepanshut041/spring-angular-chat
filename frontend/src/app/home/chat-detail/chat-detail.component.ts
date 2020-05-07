import { Component, OnInit } from '@angular/core';
import { ChatService } from 'src/app/_services/chat.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FriendProfile } from 'src/app/_dtos/chat/FriendProfile';
import { UserProfile } from 'src/app/_dtos/user/UserProfile';
import { UserService } from 'src/app/_services/user.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-chat-detail',
  templateUrl: './chat-detail.component.html',
  styleUrls: ['./chat-detail.component.scss']
})
export class ChatDetailComponent implements OnInit {

  messages: any[];
  friendId: string;
  friendProfile: FriendProfile;
  myProfile: UserProfile;

  constructor(private chatService: ChatService, private router: Router, private route: ActivatedRoute, private userService: UserService) {
    this.route.params.subscribe(params => {
      this.friendId = params['id'];
    });

    this.myProfile = this.userService.getProfile();
    this.friendProfile = this.chatService.getFriend(this.friendId);

    if(this.friendProfile == null){
      this.router.navigateByUrl("chat");
    }
  }
  ngOnInit(): void {
    this.chatService.getMessages(this.friendId).subscribe()
  }

}
