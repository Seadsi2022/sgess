import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EeventualiteFormService, EeventualiteFormGroup } from './eeventualite-form.service';
import { IEeventualite } from '../eeventualite.model';
import { EeventualiteService } from '../service/eeventualite.service';

@Component({
  selector: 'jhi-eeventualite-update',
  templateUrl: './eeventualite-update.component.html',
})
export class EeventualiteUpdateComponent implements OnInit {
  isSaving = false;
  eeventualite: IEeventualite | null = null;

  editForm: EeventualiteFormGroup = this.eeventualiteFormService.createEeventualiteFormGroup();

  constructor(
    protected eeventualiteService: EeventualiteService,
    protected eeventualiteFormService: EeventualiteFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eeventualite }) => {
      this.eeventualite = eeventualite;
      if (eeventualite) {
        this.updateForm(eeventualite);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eeventualite = this.eeventualiteFormService.getEeventualite(this.editForm);
    if (eeventualite.id !== null) {
      this.subscribeToSaveResponse(this.eeventualiteService.update(eeventualite));
    } else {
      this.subscribeToSaveResponse(this.eeventualiteService.create(eeventualite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEeventualite>>): void {
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

  protected updateForm(eeventualite: IEeventualite): void {
    this.eeventualite = eeventualite;
    this.eeventualiteFormService.resetForm(this.editForm, eeventualite);
  }
}
