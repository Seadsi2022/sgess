import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEvaleurvariable } from '../evaleurvariable.model';
import { EvaleurvariableService } from '../service/evaleurvariable.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './evaleurvariable-delete-dialog.component.html',
})
export class EvaleurvariableDeleteDialogComponent {
  evaleurvariable?: IEvaleurvariable;

  constructor(protected evaleurvariableService: EvaleurvariableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.evaleurvariableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
