import { Component, Input } from '@angular/core';
import { NbDialogRef } from '@nebular/theme';

@Component({
    template: `
    <nb-card class="dialog-card">
      <nb-card-header>{{ title }}</nb-card-header>
      <nb-card-body class="text-center">
          <p>
              <nb-icon icon="checkmark-outline" status="success" size="giant" style="font-size=6rem;"></nb-icon>
          </p>
          <p>{{ message }}</p>
      </nb-card-body>
      <nb-card-footer class="text-center">
          <button nbButton (click)="dismiss()" status="primary">Close</button>
      </nb-card-footer>
    </nb-card>
    `,
  })
export class DialogSuccessComponent {

    @Input() title: string;
    @Input() message: string;
    constructor(protected ref: NbDialogRef<DialogSuccessComponent>) {
    }

    dismiss() {
        this.ref.close();
      }
  }