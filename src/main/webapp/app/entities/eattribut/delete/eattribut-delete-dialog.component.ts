import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEattribut } from '../eattribut.model';
import { EattributService } from '../service/eattribut.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './eattribut-delete-dialog.component.html',
})
export class EattributDeleteDialogComponent {
  eattribut?: IEattribut;

  constructor(protected eattributService: EattributService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eattributService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
