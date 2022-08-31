import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISlocalite } from '../slocalite.model';
import { SlocaliteService } from '../service/slocalite.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './slocalite-delete-dialog.component.html',
})
export class SlocaliteDeleteDialogComponent {
  slocalite?: ISlocalite;

  constructor(protected slocaliteService: SlocaliteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.slocaliteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
