import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IScodevaleur } from '../scodevaleur.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../scodevaleur.test-samples';

import { ScodevaleurService } from './scodevaleur.service';

const requireRestSample: IScodevaleur = {
  ...sampleWithRequiredData,
};

describe('Scodevaleur Service', () => {
  let service: ScodevaleurService;
  let httpMock: HttpTestingController;
  let expectedResult: IScodevaleur | IScodevaleur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ScodevaleurService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Scodevaleur', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const scodevaleur = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(scodevaleur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Scodevaleur', () => {
      const scodevaleur = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(scodevaleur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Scodevaleur', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Scodevaleur', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Scodevaleur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addScodevaleurToCollectionIfMissing', () => {
      it('should add a Scodevaleur to an empty array', () => {
        const scodevaleur: IScodevaleur = sampleWithRequiredData;
        expectedResult = service.addScodevaleurToCollectionIfMissing([], scodevaleur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(scodevaleur);
      });

      it('should not add a Scodevaleur to an array that contains it', () => {
        const scodevaleur: IScodevaleur = sampleWithRequiredData;
        const scodevaleurCollection: IScodevaleur[] = [
          {
            ...scodevaleur,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScodevaleurToCollectionIfMissing(scodevaleurCollection, scodevaleur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Scodevaleur to an array that doesn't contain it", () => {
        const scodevaleur: IScodevaleur = sampleWithRequiredData;
        const scodevaleurCollection: IScodevaleur[] = [sampleWithPartialData];
        expectedResult = service.addScodevaleurToCollectionIfMissing(scodevaleurCollection, scodevaleur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scodevaleur);
      });

      it('should add only unique Scodevaleur to an array', () => {
        const scodevaleurArray: IScodevaleur[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scodevaleurCollection: IScodevaleur[] = [sampleWithRequiredData];
        expectedResult = service.addScodevaleurToCollectionIfMissing(scodevaleurCollection, ...scodevaleurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const scodevaleur: IScodevaleur = sampleWithRequiredData;
        const scodevaleur2: IScodevaleur = sampleWithPartialData;
        expectedResult = service.addScodevaleurToCollectionIfMissing([], scodevaleur, scodevaleur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scodevaleur);
        expect(expectedResult).toContain(scodevaleur2);
      });

      it('should accept null and undefined values', () => {
        const scodevaleur: IScodevaleur = sampleWithRequiredData;
        expectedResult = service.addScodevaleurToCollectionIfMissing([], null, scodevaleur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(scodevaleur);
      });

      it('should return initial array if no Scodevaleur is added', () => {
        const scodevaleurCollection: IScodevaleur[] = [sampleWithRequiredData];
        expectedResult = service.addScodevaleurToCollectionIfMissing(scodevaleurCollection, undefined, null);
        expect(expectedResult).toEqual(scodevaleurCollection);
      });
    });

    describe('compareScodevaleur', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareScodevaleur(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareScodevaleur(entity1, entity2);
        const compareResult2 = service.compareScodevaleur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareScodevaleur(entity1, entity2);
        const compareResult2 = service.compareScodevaleur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareScodevaleur(entity1, entity2);
        const compareResult2 = service.compareScodevaleur(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
