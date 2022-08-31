import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EcampagneFormService, EcampagneFormGroup } from './ecampagne-form.service';
import { IEcampagne } from '../ecampagne.model';
import { EcampagneService } from '../service/ecampagne.service';

@Component({
  selector: 'jhi-ecampagne-update',
  templateUrl: './ecampagne-update.component.html',
})
export class EcampagneUpdateComponent implements OnInit {
  isSaving = false;
  ecampagne: IEcampagne | null = null;

  editForm: EcampagneFormGroup = this.ecampagneFormService.createEcampagneFormGroup();

  constructor(
    protected ecampagneService: EcampagneService,
    protected ecampagneFormService: EcampagneFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ecampagne }) => {
      this.ecampagne = ecampagne;
      if (ecampagne) {
        this.updateForm(ecampagne);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ecampagne = this.ecampagneFormService.getEcampagne(this.editForm);
    if (ecampagne.id !== null) {
      this.subscribeToSaveResponse(this.ecampagneService.update(ecampagne));
    } else {
      this.subscribeToSaveResponse(this.ecampagneService.create(ecampagne));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEcampagne>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(ecampagne: IEcampagne): void {
    this.ecampagne = ecampagne;
    this.ecampagneFormService.resetForm(this.editForm, ecampagne);
  }
}
