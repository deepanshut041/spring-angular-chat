import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NbLayoutModule, NbCardModule, NbAlertModule, NbInputModule, NbCheckboxModule, NbFormFieldModule, NbButtonModule, NbIconModule } from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    NbLayoutModule,
    NbCardModule,
    NbAlertModule,
    NbInputModule,
    NbFormFieldModule,
    NbCheckboxModule,
    NbButtonModule,
    NbIconModule
  ], 
  exports: [
    NbLayoutModule,
    NbCardModule,
    NbAlertModule,
    NbInputModule,
    NbFormFieldModule,
    NbCheckboxModule,
    NbButtonModule,
    NbIconModule
  ]
})
export class SharedModule { }
