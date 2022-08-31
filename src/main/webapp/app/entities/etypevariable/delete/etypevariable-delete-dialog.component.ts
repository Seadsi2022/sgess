import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEtypevariable } from '../etypevariable.model';
import { EtypevariableService } from '../service/etypevariable.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './etypevariable-delete-dialog.component.html',
})
export class EtypevariableDeleteDialogComponent {
  etypevariable?: IEtypevariable;

  constructor(protected etypevariableService: EtypevariableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.etypevariableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
