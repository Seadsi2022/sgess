import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEtypechamp } from '../etypechamp.model';
import { EtypechampService } from '../service/etypechamp.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './etypechamp-delete-dialog.component.html',
})
export class EtypechampDeleteDialogComponent {
  etypechamp?: IEtypechamp;

  constructor(protected etypechampService: EtypechampService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.etypechampService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
