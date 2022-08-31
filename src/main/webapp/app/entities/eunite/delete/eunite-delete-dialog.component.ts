import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEunite } from '../eunite.model';
import { EuniteService } from '../service/eunite.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './eunite-delete-dialog.component.html',
})
export class EuniteDeleteDialogComponent {
  eunite?: IEunite;

  constructor(protected euniteService: EuniteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.euniteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
