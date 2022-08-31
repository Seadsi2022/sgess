import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISstructure } from '../sstructure.model';
import { SstructureService } from '../service/sstructure.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './sstructure-delete-dialog.component.html',
})
export class SstructureDeleteDialogComponent {
  sstructure?: ISstructure;

  constructor(protected sstructureService: SstructureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sstructureService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
