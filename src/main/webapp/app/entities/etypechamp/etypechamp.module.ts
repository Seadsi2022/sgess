import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EtypechampComponent } from './list/etypechamp.component';
import { EtypechampDetailComponent } from './detail/etypechamp-detail.component';
import { EtypechampUpdateComponent } from './update/etypechamp-update.component';
import { EtypechampDeleteDialogComponent } from './delete/etypechamp-delete-dialog.component';
import { EtypechampRoutingModule } from './route/etypechamp-routing.module';

@NgModule({
  imports: [SharedModule, EtypechampRoutingModule],
  declarations: [EtypechampComponent, EtypechampDetailComponent, EtypechampUpdateComponent, EtypechampDeleteDialogComponent],
})
export class EtypechampModule {}
