import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EeventualitevariableFormService, EeventualitevariableFormGroup } from './eeventualitevariable-form.service';
import { IEeventualitevariable } from '../eeventualitevariable.model';
import { EeventualitevariableService } from '../service/eeventualitevariable.service';
import { IEvariable } from 'app/entities/evariable/evariable.model';
import { EvariableService } from 'app/entities/evariable/service/evariable.service';
import { IEeventualite } from 'app/entities/eeventualite/eeventualite.model';
import { EeventualiteService } from 'app/entities/eeventualite/service/eeventualite.service';

@Component({
  selector: 'jhi-eeventualitevariable-update',
  templateUrl: './eeventualitevariable-update.component.html',
})
export class EeventualitevariableUpdateComponent implements OnInit {
  isSaving = false;
  eeventualitevariable: IEeventualitevariable | null = null;

  evariablesSharedCollection: IEvariable[] = [];
  eeventualitesSharedCollection: IEeventualite[] = [];

  editForm: EeventualitevariableFormGroup = this.eeventualitevariableFormService.createEeventualitevariableFormGroup();

  constructor(
    protected eeventualitevariableService: EeventualitevariableService,
    protected eeventualitevariableFormService: EeventualitevariableFormService,
    protected evariableService: EvariableService,
    protected eeventualiteService: EeventualiteService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEvariable = (o1: IEvariable | null, o2: IEvariable | null): boolean => this.evariableService.compareEvariable(o1, o2);

  compareEeventualite = (o1: IEeventualite | null, o2: IEeventualite | null): boolean =>
    this.eeventualiteService.compareEeventualite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eeventualitevariable }) => {
      this.eeventualitevariable = eeventualitevariable;
      if (eeventualitevariable) {
        this.updateForm(eeventualitevariable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eeventualitevariable = this.eeventualitevariableFormService.getEeventualitevariable(this.editForm);
    if (eeventualitevariable.id !== null) {
      this.subscribeToSaveResponse(this.eeventualitevariableService.update(eeventualitevariable));
    } else {
      this.subscribeToSaveResponse(this.eeventualitevariableService.create(eeventualitevariable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEeventualitevariable>>): void {
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

  protected updateForm(eeventualitevariable: IEeventualitevariable): void {
    this.eeventualitevariable = eeventualitevariable;
    this.eeventualitevariableFormService.resetForm(this.editForm, eeventualitevariable);

    this.evariablesSharedCollection = this.evariableService.addEvariableToCollectionIfMissing<IEvariable>(
      this.evariablesSharedCollection,
      eeventualitevariable.evariable
    );
    this.eeventualitesSharedCollection = this.eeventualiteService.addEeventualiteToCollectionIfMissing<IEeventualite>(
      this.eeventualitesSharedCollection,
      eeventualitevariable.eeventualite
    );
  }

  protected loadRelationshipsOptions(): void {
    this.evariableService
      .query()
      .pipe(map((res: HttpResponse<IEvariable[]>) => res.body ?? []))
      .pipe(
        map((evariables: IEvariable[]) =>
          this.evariableService.addEvariableToCollectionIfMissing<IEvariable>(evariables, this.eeventualitevariable?.evariable)
        )
      )
      .subscribe((evariables: IEvariable[]) => (this.evariablesSharedCollection = evariables));

    this.eeventualiteService
      .query()
      .pipe(map((res: HttpResponse<IEeventualite[]>) => res.body ?? []))
      .pipe(
        map((eeventualites: IEeventualite[]) =>
          this.eeventualiteService.addEeventualiteToCollectionIfMissing<IEeventualite>(
            eeventualites,
            this.eeventualitevariable?.eeventualite
          )
        )
      )
      .subscribe((eeventualites: IEeventualite[]) => (this.eeventualitesSharedCollection = eeventualites));
  }
}
