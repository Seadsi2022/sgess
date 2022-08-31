import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEvariable } from '../evariable.model';
import { EvariableService } from '../service/evariable.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './evariable-delete-dialog.component.html',
})
export class EvariableDeleteDialogComponent {
  evariable?: IEvariable;

  constructor(protected evariableService: EvariableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.evariableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
