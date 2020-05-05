import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';

import { HomeComponent } from './home.component';
import { ChatComponent } from './chat/chat.component';
import { ProfileComponent } from './profile/profile.component';
import { SettingsComponent } from './settings/settings.component';

import { HomeRoutingModule } from './home-routing.module';
import { NbSidebarService, NbMenuService } from '@nebular/theme';
import { ChatListComponent } from './chat-list/chat-list.component';
import { UserService } from '../_services/user.service';
import { ChatService } from '../_services/chat.service';
import { NotificationService } from '../_services/notification.service';

@NgModule({
  declarations: [
    HomeComponent,
    ChatComponent,
    ProfileComponent,
    SettingsComponent,
    ChatListComponent
  ],
  imports: [
    CommonModule, HomeRoutingModule, SharedModule,
  ],
  providers:[
    NbSidebarService, NbMenuService, UserService, ChatService, NotificationService
  ]
})
export class HomeModule { }
