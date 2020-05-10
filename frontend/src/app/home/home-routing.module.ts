import { NgModule } from '@angular/core';

import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home.component';
import { SettingsComponent } from './settings/settings.component';
import { AuthGuard } from '../_helpers/auth.guard';
import { LoadingComponent } from './loading/loading.component';
import { ChatComponent } from './chat/chat.component';
import { ChatDetailComponent } from './chat-detail/chat-detail.component';
import { ChatBannerComponent } from './chat-banner/chat-banner.component';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes = [
  {
    path: '', component: HomeComponent, canActivate: [AuthGuard], children: [
      {
        path: 'chat', component: ChatComponent, children: [
          { path: '', component: ChatBannerComponent },
          { path: ':id', component: ChatDetailComponent },
        ]
      },
      { path: 'profile', component: ProfileComponent},
      { path: 'loading', component: LoadingComponent },
      { path: 'settings', component: SettingsComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HomeRoutingModule {

}
