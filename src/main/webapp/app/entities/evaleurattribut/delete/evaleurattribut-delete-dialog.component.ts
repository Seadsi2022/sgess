import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEvaleurattribut } from '../evaleurattribut.model';
import { EvaleurattributService } from '../service/evaleurattribut.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './evaleurattribut-delete-dialog.component.html',
})
export class EvaleurattributDeleteDialogComponent {
  evaleurattribut?: IEvaleurattribut;

  constructor(protected evaleurattributService: EvaleurattributService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.evaleurattributService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
