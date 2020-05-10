import { Component, OnInit } from '@angular/core';
import { NbMenuService, NbDialogService } from '@nebular/theme';
import { filter, map } from 'rxjs/operators';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/_services/user.service';
import { UserProfile } from 'src/app/_dtos/user/UserProfile';
import { ChatService } from 'src/app/_services/chat.service';
import { Observable } from 'rxjs';
import { FriendProfile } from 'src/app/_dtos/chat/FriendProfile';
import { NewChatComponent } from './new-chat/new-chat.component';

@Component({
  selector: 'home-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss']
})
export class ChatListComponent implements OnInit {

  friends: Observable<FriendProfile[]>
  menu = [
    { title: 'Profile', icon: 'person-outline' },
    { title: 'New Chat', icon: 'person-add-outline' },
    { title: 'New Group', icon: 'plus-outline' },
    { title: 'Settings', icon: 'settings-outline' },
    { title: 'Log out', icon: 'unlock-outline' },
  ];

  profile: UserProfile

  constructor(private menuService: NbMenuService, private router: Router, private dialogService: NbDialogService,
    private userService: UserService, private chatService: ChatService, private route: ActivatedRoute) {
    this.profile = this.userService.getProfile()
    this.friends = this.chatService.getFriends()
  }

  ngOnInit(): void {
    this.menuListener()
  }

  menuListener() {
    this.menuService.onItemClick()
      .pipe(
        filter(({ tag }) => tag === 'context-chat-more'),
        map(({ item: { title } }) => title),
      )
      .subscribe(title => {
        switch (title) {
          case 'Profile':
            this.router.navigateByUrl("/profile")
            break;
          case 'New Chat':
            this.dialogService.open(NewChatComponent).onClose.subscribe((email) => {
              this.chatService.createFriend(email).subscribe(
                (r) => { console.log(r) },
                (err) => { console.log(err) }
              )
            })
            break;
          case 'New Group':

            break;
          case 'Settings':
            this.router.navigateByUrl("/settings")
            break;
          case 'Log out':
            this.userService.logout()
            this.router.navigateByUrl("/auth")
            break;
          default:
            break;
        }
      })
  }

  chatClicked(id: String){
    this.router.navigate([id], { relativeTo: this.route, skipLocationChange:true })
  }

}
