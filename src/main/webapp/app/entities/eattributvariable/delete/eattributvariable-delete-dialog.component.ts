import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEattributvariable } from '../eattributvariable.model';
import { EattributvariableService } from '../service/eattributvariable.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './eattributvariable-delete-dialog.component.html',
})
export class EattributvariableDeleteDialogComponent {
  eattributvariable?: IEattributvariable;

  constructor(protected eattributvariableService: EattributvariableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eattributvariableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
