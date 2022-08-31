import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEeventualitevariable } from '../eeventualitevariable.model';
import { EeventualitevariableService } from '../service/eeventualitevariable.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './eeventualitevariable-delete-dialog.component.html',
})
export class EeventualitevariableDeleteDialogComponent {
  eeventualitevariable?: IEeventualitevariable;

  constructor(protected eeventualitevariableService: EeventualitevariableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eeventualitevariableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
