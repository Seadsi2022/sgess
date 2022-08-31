import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EuniteFormService, EuniteFormGroup } from './eunite-form.service';
import { IEunite } from '../eunite.model';
import { EuniteService } from '../service/eunite.service';

@Component({
  selector: 'jhi-eunite-update',
  templateUrl: './eunite-update.component.html',
})
export class EuniteUpdateComponent implements OnInit {
  isSaving = false;
  eunite: IEunite | null = null;

  eunitesSharedCollection: IEunite[] = [];

  editForm: EuniteFormGroup = this.euniteFormService.createEuniteFormGroup();

  constructor(
    protected euniteService: EuniteService,
    protected euniteFormService: EuniteFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEunite = (o1: IEunite | null, o2: IEunite | null): boolean => this.euniteService.compareEunite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eunite }) => {
      this.eunite = eunite;
      if (eunite) {
        this.updateForm(eunite);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eunite = this.euniteFormService.getEunite(this.editForm);
    if (eunite.id !== null) {
      this.subscribeToSaveResponse(this.euniteService.update(eunite));
    } else {
      this.subscribeToSaveResponse(this.euniteService.create(eunite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEunite>>): void {
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

  protected updateForm(eunite: IEunite): void {
    this.eunite = eunite;
    this.euniteFormService.resetForm(this.editForm, eunite);

    this.eunitesSharedCollection = this.euniteService.addEuniteToCollectionIfMissing<IEunite>(
      this.eunitesSharedCollection,
      eunite.unitebase
    );
  }

  protected loadRelationshipsOptions(): void {
    this.euniteService
      .query()
      .pipe(map((res: HttpResponse<IEunite[]>) => res.body ?? []))
      .pipe(map((eunites: IEunite[]) => this.euniteService.addEuniteToCollectionIfMissing<IEunite>(eunites, this.eunite?.unitebase)))
      .subscribe((eunites: IEunite[]) => (this.eunitesSharedCollection = eunites));
  }
}
