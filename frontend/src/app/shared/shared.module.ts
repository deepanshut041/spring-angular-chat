import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  NbLayoutModule, NbCardModule, NbAlertModule, NbInputModule, NbCheckboxModule, NbFormFieldModule, NbButtonModule, NbIconModule,
  NbSpinnerModule, NbUserModule, NbSidebarModule, NbChatModule, NbListModule, NbContextMenuModule, NbDialogModule
} from '@nebular/theme';
import { DialogSuccessComponent } from './dialog/dialog-alert/dialog-success.component';



@NgModule({
  declarations: [DialogSuccessComponent],
  imports: [
    CommonModule, NbLayoutModule, NbCardModule, NbAlertModule, NbInputModule, NbFormFieldModule, NbCheckboxModule,
    NbButtonModule, NbIconModule, NbSpinnerModule, NbUserModule, NbSidebarModule, NbChatModule, NbListModule, NbContextMenuModule,
    NbDialogModule
  ],
  exports: [
    NbLayoutModule, NbCardModule, NbAlertModule, NbInputModule, NbFormFieldModule, NbCheckboxModule, NbButtonModule, NbIconModule,
    NbSpinnerModule, NbUserModule, NbSidebarModule, NbChatModule, NbListModule, NbContextMenuModule, NbDialogModule,
    DialogSuccessComponent,
  ]
})
export class SharedModule { }
