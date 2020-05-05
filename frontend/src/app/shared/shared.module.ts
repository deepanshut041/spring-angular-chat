import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  NbLayoutModule, NbCardModule, NbAlertModule, NbInputModule, NbCheckboxModule, NbFormFieldModule, NbButtonModule, NbIconModule,
  NbSpinnerModule, NbUserModule, NbSidebarModule, NbChatModule, NbListModule, NbContextMenuModule, NbDialogModule, NbProgressBarModule
} from '@nebular/theme';
import { DialogSuccessComponent } from './dialog/dialog-alert/dialog-success.component';
import { ImgFallbackDirective } from './directives/img-fallback.directive';



@NgModule({
  declarations: [DialogSuccessComponent, ImgFallbackDirective],
  imports: [
    CommonModule, NbLayoutModule, NbCardModule, NbAlertModule, NbInputModule, NbFormFieldModule, NbCheckboxModule, NbProgressBarModule,
    NbButtonModule, NbIconModule, NbSpinnerModule, NbUserModule, NbSidebarModule, NbChatModule, NbListModule, NbContextMenuModule,
    NbDialogModule
  ],
  exports: [
    NbLayoutModule, NbCardModule, NbAlertModule, NbInputModule, NbFormFieldModule, NbCheckboxModule, NbButtonModule, NbIconModule,
    NbSpinnerModule, NbUserModule, NbSidebarModule, NbChatModule, NbListModule, NbContextMenuModule, NbDialogModule, NbProgressBarModule,
    DialogSuccessComponent,
    ImgFallbackDirective,
  ]
})
export class SharedModule { }
