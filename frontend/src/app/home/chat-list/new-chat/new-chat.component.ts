import { Component, Input } from '@angular/core';
import { NbDialogRef } from '@nebular/theme';

@Component({
    template: `
    <nb-card class="dialog-card">
      <nb-card-header>Enter Your Friend Email</nb-card-header>
      <nb-card-body>
        <input #email nbInput placeholder="Email" type="email">
      </nb-card-body>
      <nb-card-footer class="text-center">
        <button nbButton (click)="submit(email.value)" status="primary" class="m-2">Submit</button>
        <button nbButton (click)="dismiss()" status="danger" class="m-2">Close</button>
      </nb-card-footer>
    </nb-card>
    `,
})
export class NewChatComponent {

    constructor(protected ref: NbDialogRef<NewChatComponent>) {
    }

    dismiss() {
        this.ref.close();
    }
    
    submit(email: string){
        this.ref.close(email);
    }
}