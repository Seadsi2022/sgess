import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EcampagneComponent } from './list/ecampagne.component';
import { EcampagneDetailComponent } from './detail/ecampagne-detail.component';
import { EcampagneUpdateComponent } from './update/ecampagne-update.component';
import { EcampagneDeleteDialogComponent } from './delete/ecampagne-delete-dialog.component';
import { EcampagneRoutingModule } from './route/ecampagne-routing.module';

@NgModule({
  imports: [SharedModule, EcampagneRoutingModule],
  declarations: [EcampagneComponent, EcampagneDetailComponent, EcampagneUpdateComponent, EcampagneDeleteDialogComponent],
})
export class EcampagneModule {}
